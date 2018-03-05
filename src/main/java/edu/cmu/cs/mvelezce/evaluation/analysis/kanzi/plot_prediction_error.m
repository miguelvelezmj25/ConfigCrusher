x = [7, 29, 64, 64];
y = [1.86, 1.29, 1.21, 2.66];
n = ["PW", "FW"', "SAD"', "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Kanzi');
ylim([1 3])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/kanzi/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/kanzi/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')