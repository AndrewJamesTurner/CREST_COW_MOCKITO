#! /usr/bin/octave -qf
clc;
clear all;
close all;

graphics_toolkit ("fltk") %  fltk gnuplot

fontSize = 12;

data = csvread("fiveHoursPopSize100.csv");

x = data(:,1);
y = data(:,2);

scatter(x*100,y*100, 10, [], 'x');

xlabel('Reduction in test coverage (%)', 'FontSize', fontSize)
ylabel('Reduction in run time (%)', 'FontSize', fontSize)

set(gca,'fontsize',fontSize)

%saveas(gcf, 'fiveHoursPopSize100.pdf')
%system('pdfcrop fiveHoursPopSize100.pdf fiveHoursPopSize100.pdf')