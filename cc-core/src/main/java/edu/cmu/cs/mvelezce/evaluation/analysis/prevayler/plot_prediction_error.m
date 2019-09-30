x = [9, 46, 144, 32];
y = [111.21, 29.23, 2.95, 9.23];
n = ["FW", "PW", "SAD", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Prevayler');
% xlim([-10 400])
fontset
 
fig = gcf;
fig.PaperPositionMode = 'auto';
fig_pos = fig.PaperPosition;
fig.PaperSize = [fig_pos(3) fig_pos(4)];

mkdir('../../../../../../../../resources/evaluation/programs/java/prevayler/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/prevayler/plots/prediction_error.pdf';
print(fig, fileID,'-dpdf');
fileID = '../../../../../../../../resources/evaluation/programs/java/prevayler/plots/prediction_error.png';
print(fig, fileID,'-dpng');