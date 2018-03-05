x = [10, 56, 512, 8];
y = [56.91, 6.22, 0.18, 0.07];
n = ["PW", "FW", "SAD", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Running example');
xlim([-5 600])
ylim([-2 60])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/running-example/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/running-example/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')