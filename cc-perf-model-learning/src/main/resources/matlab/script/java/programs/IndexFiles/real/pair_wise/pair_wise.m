train = readtable('../../../../../../../data/java/programs/IndexFiles/real/pair_wise/pair_wise.csv');
x_train = table2array(train(:,1:17));
y_train = table2array(train(:,18:18));
model = stepwiselm(x_train, y_train, 'interactions');

mkdir('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/IndexFiles/real/pair_wise');

terms = model.Coefficients.Row;
fileID = fopen('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/IndexFiles/real/pair_wise/terms.txt', 'w');
fprintf(fileID, '%s\n', terms{:});
fclose(fileID);

coefs = model.Coefficients.Estimate;
fileID = fopen('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/IndexFiles/real/pair_wise/coefs.txt', 'w');
fprintf(fileID, '%10.2f\n', coefs);
fclose(fileID);

pValues = model.Coefficients.pValue;
fileID = fopen('../../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/IndexFiles/real/pair_wise/pValues.txt', 'w');
fprintf(fileID, '%3.2f\n', pValues);
fclose(fileID);

model
