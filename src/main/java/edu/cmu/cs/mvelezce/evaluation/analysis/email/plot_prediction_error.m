x = [4, 11, 1];
y = [100, 44.41, 2.34];
n = ["PW", "FW"', "FB"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Email');
% xlim([-10 400])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/email/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/email/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')