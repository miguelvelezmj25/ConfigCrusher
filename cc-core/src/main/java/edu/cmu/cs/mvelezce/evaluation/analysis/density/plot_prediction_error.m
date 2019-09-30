x = [22, 254, 256];
y = [318.12, 175.16, 4.32];
n = ["FW", "PW", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Density Converter');
ylim([-10 350]);
% yticks(linspace(0,350,8));
fontset;
 
fig = gcf;
fig.PaperPositionMode = 'auto';
fig_pos = fig.PaperPosition;
fig.PaperSize = [fig_pos(3) fig_pos(4)];

mkdir('../../../../../../../../resources/evaluation/programs/java/density/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/density/plots/prediction_error.pdf';
print(fig, fileID,'-dpdf');
fileID = '../../../../../../../../resources/evaluation/programs/java/density/plots/prediction_error.png';
print(fig, fileID,'-dpng');