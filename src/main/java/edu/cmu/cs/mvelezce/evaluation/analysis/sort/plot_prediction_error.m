x = [12, 79, 48, 256];
y = [89.96, 653.03, 2.38, 1.75];
n = ["PW", "FW", "SAD", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('sort');
ylim([-20 700])
fontset

 
% mkdir('../../../../../../../../resources/evaluation/programs/java/sort/plots/');
% fileID = '../../../../../../../../resources/evaluation/programs/java/sort/plots/prediction_error.pdf';
% print(fileID,'-dpdf','-fillpage')