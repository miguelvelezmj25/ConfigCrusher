train = readtable('../../../../../../../data/java/programs/RunBenchC/real/random50/random50.csv');
x_train = table2array(train(:,1:16));
y_train = table2array(train(:,17:17));
model = stepwiselm(x_train, y_train, 'interactions');

mkdir('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/RunBenchC/real/random50');

terms = model.Coefficients.Row;
fileID = fopen('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/RunBenchC/real/random50/terms.txt', 'w');
fprintf(fileID, '%s\n', terms{:});
fclose(fileID);

coefs = model.Coefficients.Estimate;
fileID = fopen('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/RunBenchC/real/random50/coefs.txt', 'w');
fprintf(fileID, '%10.2f\n', coefs);
fclose(fileID);

pValues = model.Coefficients.pValue;
fileID = fopen('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/RunBenchC/real/random50/pValues.txt', 'w');
fprintf(fileID, '%3.2f\n', pValues);
fclose(fileID);

model
