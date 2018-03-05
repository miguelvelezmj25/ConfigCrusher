x = [5, 16, 24, 4];
y = [0.8, 1.94, 1.33, 1.1];
n = ["PW", "FW"', "SAD", "CC"];

scatter(x, y, 1500, '.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Pngtastic Counter');
ylim([0.7 2])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/pngtasticColorCounter/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/pngtasticColorCounter/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')