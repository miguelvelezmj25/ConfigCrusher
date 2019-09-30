x = [4, 11, 1, 256];
y = [100, 44.23, 2.34, 23.02];
n = ["FW", "PW"', "FB", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Email');
ylim([0 120])
fontset
 
fig = gcf;
fig.PaperPositionMode = 'auto';
fig_pos = fig.PaperPosition;
fig.PaperSize = [fig_pos(3) fig_pos(4)];

mkdir('../../../../../../../../resources/evaluation/programs/java/email/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/email/plots/prediction_error.pdf';
print(fig, fileID,'-dpdf');
fileID = '../../../../../../../../resources/evaluation/programs/java/email/plots/prediction_error.png';
print(fig, fileID,'-dpng');