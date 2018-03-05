x = [3, 9, 1];
y = [51.09, 1.48, 2.72];
n = ["PW", "FW"', "FB"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Elevator');
% xlim([-10 400])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/elevator/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/elevator/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')