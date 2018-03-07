x = [4, 11, 1];
y = [100, 44.41, 2.34];
n = ["FW", "PW"', "FB"];

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