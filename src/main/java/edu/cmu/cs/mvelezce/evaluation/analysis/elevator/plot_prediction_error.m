x = [3, 9, 1];
y = [51.09, 1.48, 2.72];
n = ["FW", "PW"', "FB"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Elevator');
% xlim([-10 400])
% yticks(linspace(0,350,8));
fontset;
 
fig = gcf;
fig.PaperPositionMode = 'auto';
fig_pos = fig.PaperPosition;
fig.PaperSize = [fig_pos(3) fig_pos(4)];
 
mkdir('../../../../../../../../resources/evaluation/programs/java/elevator/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/elevator/plots/prediction_error.pdf';
print(fig, fileID,'-dpdf');
fileID = '../../../../../../../../resources/evaluation/programs/java/elevator/plots/prediction_error.png';
print(fig, fileID,'-dpng');