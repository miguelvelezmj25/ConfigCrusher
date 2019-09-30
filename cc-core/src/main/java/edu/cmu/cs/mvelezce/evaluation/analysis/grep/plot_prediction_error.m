x = [7, 29, 48, 64];
y = [32.14, 114.74, 1.94, 3.58];
n = ["FW", "PW", "SAD", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('grep');
% xlim([-10 400])
fontset
 
fig = gcf;
fig.PaperPositionMode = 'auto';
fig_pos = fig.PaperPosition;
fig.PaperSize = [fig_pos(3) fig_pos(4)];

mkdir('../../../../../../../../resources/evaluation/programs/java/grep/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/grep/plots/prediction_error.pdf';
print(fig, fileID,'-dpdf');
fileID = '../../../../../../../../resources/evaluation/programs/java/grep/plots/prediction_error.png';
print(fig, fileID,'-dpng');