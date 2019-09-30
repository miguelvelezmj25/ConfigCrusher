x = [5, 16, 10];
y = [19.67, 0.99, 1.07];
n = ["FW", "PW/SAD", "CC"];

scatter(x,y,1500,'.');
text(x, y, n, 'horizontal','left', 'vertical','bottom')
xlabel('Cost [Configurations]');
ylabel('Prediction Error [MAPE]');
title('Pngtastic Optimizer');
xlim([4 20])
fontset

fig = gcf;
fig.PaperPositionMode = 'auto';
fig_pos = fig.PaperPosition;
fig.PaperSize = [fig_pos(3) fig_pos(4)]; 

mkdir('../../../../../../../../resources/evaluation/programs/java/pngtasticOptimizer/plots/');
fileID = '../../../../../../../../resources/evaluation/programs/java/pngtasticOptimizer/plots/prediction_error.pdf';
print(fig, fileID,'-dpdf');
fileID = '../../../../../../../../resources/evaluation/programs/java/pngtasticOptimizer/plots/prediction_error.png';
print(fig, fileID,'-dpng');