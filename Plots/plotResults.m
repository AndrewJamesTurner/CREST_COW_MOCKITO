#! /usr/bin/octave -qf
clc;
clear all;
close all;

graphics_toolkit ("fltk") %  fltk gnuplot

fontSize = 12;

data = csvread("fiveHoursPopSize100.csv");

x = data(:,1);
y = data(:,2);

% add the point (1,1)
x(size(x)) = 0;
y(size(y)) = 1;

scatter(x*100,y*100, 10, [], 'x');



xlabel('Reduction in original branch coverage (%)', 'FontSize', fontSize)
ylabel('Proportion of original run time (%)', 'FontSize', fontSize)

axis([-0.5 8 40 105])

x1 = 0.15;
y1 = 100;
txt1 = '(original test suite)';
text(x1,y1,txt1, 'FontSize', fontSize);

set(gca,'fontsize',fontSize);

print -dpdf -color fiveHoursPopSize100.pdf
%saveas(gcf, 'fiveHoursPopSize100.pdf')
system('pdfcrop fiveHoursPopSize100.pdf fiveHoursPopSize100.pdf')



