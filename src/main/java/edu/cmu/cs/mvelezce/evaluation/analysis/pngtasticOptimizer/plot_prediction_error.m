x = [5, 16, 10];
y = [19.67, 0.99, 1.07];
n = ["PW", "FW/SAD", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Pngtastic Optimizer');
xlim([4 20])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/pngtasticOptimizer/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/pngtasticOptimizer/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')