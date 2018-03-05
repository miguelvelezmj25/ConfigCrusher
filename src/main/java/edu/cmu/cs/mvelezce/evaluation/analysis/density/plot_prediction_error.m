x = [22, 254, 256];
y = [318.12, 175.16, 4.32];
n = ["PW", "FW", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Density Converter');
% xlim([-10 400])
fontset
 
mkdir('../../../../../../../../resources/evaluation/programs/java/density/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/density/plots/prediction_error.pdf';
print(fileID,'-dpdf','-fillpage')