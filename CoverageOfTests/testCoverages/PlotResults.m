#! /usr/bin/octave -qf

clc;
clear all;
close all;

graphics_toolkit ("fltk") %  fltk gnuplot

fontSize = 12;

all = csvread('allTests.csv');
sol = csvread('solution_0.526061_4.304.csv');


className = {"DefaultMockitoConfiguration" "MockitoAssertionError" "MockitoException" "MockitoSerializationIssue" "CannotStubVoidMethodWithReturnValue" "CannotVerifyStubOnlyMock" "FriendlyReminderException" "InvalidUseOfMatchersException" "MissingMethodInvocationException" "MockitoConfigurationException" "NotAMockException" "NullInsteadOfMockException" "UnfinishedVerificationException" "WrongTypeOfReturnValue" "ArgumentsAreDifferent" "JUnitTool" "ArgumentsAreDifferent" "NeverWantedButInvoked" "NoInteractionsWanted" "SmartNullPointerException" "TooLittleActualInvocations" "TooManyActualInvocations" "VerificationInOrderFailure" "WantedButNotInvoked" "Discrepancy" "Pluralizer" "Reporter" "MockitoHamcrest" "NameBasedCandidateFilter" "OngoingInjector" "TerminalMockCandidateFilter" "TypeBasedCandidateFilter" "InjectMocksScanner" "MockScanner" "ConstructorInjection" "MockInjection" "MockInjectionStrategy" "PropertyAndSetterInjection" "SpyOnInjectedFieldsHandler"};

LineStart = [1 6 16 27 36 38 42 46 48 52 54 58 60 62 71 74 77 79 81 83 85 87 89 92 94 95 247 261 274 276 285 292 304 320 339 364 372 403];


class = 26;

all = all(2:end, 3:4);
sol = sol(2:end, 3:4);

lines = all(LineStart(class):LineStart(class+1) -1,1);
coverageAll = all(LineStart(class):LineStart(class+1) -1, 2);
coverageSol = sol(LineStart(class):LineStart(class+1) -1,2);

% remove black lines
%lines = lines(coverageAll > 0);
%coverageAll = coverageAll(coverageAll > 0);
%coverageSol = coverageSol(coverageAll > 0);


bar( lines, coverageAll, 'b');



%bar( coverageAll, 'b');

hold on;



bar( lines, coverageSol, 'r');

%bar(  coverageSol, 'r');


hold off;

%set(gca, 'XTickLabel', [])



%a=axes;
%set(a,'xticklabel',[]);

xlabel('Line In File', 'FontSize', fontSize)
ylabel('Times Covered', 'FontSize', fontSize)

set(gca,'fontsize',fontSize)

%print -dpdf -color className{class}

print('-dpdf', '-color', sprintf('%s',className{class}))

%saveas(gcf, 'fiveHoursPopSize100.pdf')
%system('pdfcrop fiveHoursPopSize100.pdf fiveHoursPopSize100.pdf')
