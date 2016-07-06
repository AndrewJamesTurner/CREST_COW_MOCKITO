#! /usr/bin/octave -qf

clc;
clear all;
close all;

graphics_toolkit ("fltk") %  fltk gnuplot

fontSize = 12;

fidAll = fopen('allTests.csv');
fidSol = fopen('solution_0.526061_4.304.csv');

allData = textscan(fidAll, '%s %s %d %d %d %d', 'delimiter', ',');
solData = textscan(fidSol, '%s %s %d %d %d %d', 'delimiter', ',');

classes = allData{2};
classes = classes(2:end, :);

lines = allData{3};
lines = lines(2:end, :);

allCoverage = allData{4};
allCoverage = allCoverage(2:end, :);

solCoverage = solData{4};
solCoverage = solCoverage(2:end, :);


lineStarts(1) = 1;
unequeClasses(1) = classes(1);

for i = 1 : size(lines, 1)
	if( isequal( classes(i), unequeClasses(size(unequeClasses, 2)) ) == 0  )
		lineStarts(size(lineStarts,2) + 1) = i;
		unequeClasses(size(unequeClasses, 2) + 1) = classes(i);
	end
end





all = csvread('allTests.csv');
sol = csvread('solution_0.526061_4.304.csv');

all = all(2:end, 3:4);
sol = sol(2:end, 3:4);


class = find(strcmp(unequeClasses, 'AtLeastXNumberOfInvocationsChecker')); % AtMost AtLeastXNumberOfInvocationsChecker
class = class(1);

lines = all(lineStarts(class):lineStarts(class+1) -1,1);
coverageAll = all(lineStarts(class):lineStarts(class+1) -1, 2);
coverageSol = sol(lineStarts(class):lineStarts(class+1) -1,2);

[lines, I] = sort(lines);
coverageAll = coverageAll(I);
coverageSol = coverageSol(I);

coverage = [coverageAll coverageSol];


b = bar( lines, coverage);

set(b(1),'facecolor','red');
set(b(2),'facecolor','blue');

legend('All Tests', 'Subset', 'location', 'northeast')

%bar( lines, coverageAll, 'b');
%bar( coverageAll, 'b');

%hold on;

%bar( lines, coverageSol, 'r');
%bar(  coverageSol, 'r');

%hold off;

xlabel('Line In File', 'FontSize', fontSize)
ylabel('Times Covered', 'FontSize', fontSize)

print('-dpdf', '-color', sprintf('%s',unequeClasses{class}))






