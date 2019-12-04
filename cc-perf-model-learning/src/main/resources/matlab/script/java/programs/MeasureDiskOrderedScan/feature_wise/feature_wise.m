train = readtable('../../../../../../data/java/programs/MeasureDiskOrderedScan/feature_wise/feature_wise.csv');
x_train = table2array(train(:,1:5));
y_train = table2array(train(:,6:6));
model = stepwiselm(x_train, y_train, 'linear');

mkdir('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise');

terms = model.Coefficients.Row;
fileID = fopen('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise/terms.txt', 'w');
fprintf(fileID, '%s\n', terms{:});
fclose(fileID);

coefs = model.Coefficients.Estimate;
fileID = fopen('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise/coefs.txt', 'w');
fprintf(fileID, '%10.2f\n', coefs);
fclose(fileID);

pValues = model.Coefficients.pValue;
fileID = fopen('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise/pValues.txt', 'w');
fprintf(fileID, '%3.2f\n', pValues);
fclose(fileID);
