import sys
import os
import re
import csv

# Takes a list of dirs
# Each element is a top-level directory for a trace
# Parse the class traces in the relevant subdirectories
# Produce counts of coverage
# Merge them (sum them)
# Bob's your father's brother

HEADER = ['package', 'class', 'line', 'full_coverage', 'partial_coverage', 'no_coverage']
OUTPUT_FILENAME = "out.csv" # Hardcoded. Forgive me, Pragmatic Programmer.
ignore_list = ['.DS_Store', '.resources', 'index.html', OUTPUT_FILENAME]


# Run through each trace directory, parse the traces.
# Produce a dictionary of each mapping LOC -> coveraged
# Then merge those dictionaries to produce a CSV-ready list
def produce_csv(directories, output_filename):
    directories_tracepoints = []
    for directory in directories:
        directories_tracepoints.append(parse_trace(directory))
    tracepoints = merge_tracepoints(directories_tracepoints)
    write_tracepoints(tracepoints, output_filename)


# For each LOC found in the first trace
# Run through the other traces and sum the coverage
# Place in a list, each element is a dictionary containing fields for CSV output
def merge_tracepoints(directories_tracepoints):
    merged = []
    for loc in directories_tracepoints[0].keys():
        loc_data = {}
        loc_data['package'] = loc[0]
        loc_data['class'] = loc[1]
        loc_data['line'] = loc[2]
        full_count = 0
        not_count = 0
        partial_count = 0
        for dir in directories_tracepoints:
            full_count += dir[loc]['full_coverage']
            not_count += dir[loc]['no_coverage']
            partial_count += dir[loc]['partial_coverage']
        loc_data['full_coverage'] = full_count
        loc_data['no_coverage'] = not_count
        loc_data['partial_coverage'] = partial_count
        merged.append(loc_data)
    sorted_merged = sorted(merged, key = lambda k: k['package'] + k['class'] + str(k['line']))
    return sorted_merged


# Run through each package directory and parse the traces in it
def parse_trace(directory):
    all_tracepoints = {}
    package_dirs = [d for d in os.listdir(directory) if d not in ignore_list]
    for package in package_dirs:
        all_tracepoints.update(parse_package(directory, package))
    return all_tracepoints


# Parse traces for each class in a package
def parse_package(directory, package):
    html_files = [f for f in os.listdir(directory + os.sep + package) if '.java' in f]
    package_tracepoints = {}
    for classTrace in html_files:
        package_tracepoints.update(parse_class(directory, package, classTrace))
    return package_tracepoints


# Parse the class trace using simple regexes.
# Store the coverage numerically so we can easily merged later on.
# Result is a dictionary mapping LOC -> coverage counts
# we're looking for <span class="nc" id="L196">
# id is the line number. NC = not covered? FC = covered? PC = Partial?
def parse_class(directory, package, classTrace):
    tracepoints = {}
    with open(directory + os.sep + package + os.sep + classTrace) as source:
        content = source.read()
        loc_list = re.findall(r'<span class=".." id="L\d+"', content)
        for loc in loc_list:
            line_number = int(re.search(r'L\d+', loc).group(0)[1:])
            covered_text = re.search(r'class=\"..', loc).group(0)[7:]
            full_coverage = 0
            no_coverage = 0
            partial_coverage = 0
            if covered_text == 'fc':
                full_coverage = 1
            elif covered_text == 'nc':
                no_coverage = 1
            elif covered_text == 'pc':
                partial_coverage = 1
            else:
                print "Coverage type not recognised!"
                print "At " + package + "." + classTrace + ":" + line_number
                sys.exit(-1)
            className = classTrace.replace(".java.html",'')
            line_key = (package, className, line_number)
            data = {'full_coverage': full_coverage, 'no_coverage':no_coverage, 'partial_coverage':partial_coverage}
            tracepoints[line_key] = data
    return tracepoints


# Dump the merged file to a CSV
def write_tracepoints(tracepoints, filename):
    with open(filename, 'w') as outfile:
        csvwriter = csv.DictWriter(outfile, fieldnames=HEADER)
        csvwriter.writeheader()
        csvwriter.writerows(tracepoints)


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print "Usage: python mockread.py <list of dirs>\n"
        print "Where each directory is the root of a trace directory, i.e. the dir itself contains subdirs for each package"
        print "This program will then parse each trace directory, and merge together the coverage"
        sys.exit(0)
    directories = sys.argv[1:]
    produce_csv(directories, OUTPUT_FILENAME)
