% feature_wise

train = readtable('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise.csv');
x_train = table2array(train(:,1:11));
y_train = table2array(train(:,12:12));
model = stepwiselm(x_train, y_train, 'linear');

mkdir('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise/');

terms = model.Coefficients.Row;
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise/terms.txt', 'w');
fprintf(fileID, '%s\n', terms{:})
fclose(fileID);

coefs = model.Coefficients.Estimate;
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise/coefs.txt', 'w');
fprintf(fileID, '%10.2f\n', coefs)
fclose(fileID);

pValues = model.Coefficients.pValue
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise/pValues.txt', 'w');
fprintf(fileID, '%3.2f\n', pValues)
fclose(fileID);

% pair_wise

train = readtable('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise.csv');
x_train = table2array(train(:,1:11));
y_train = table2array(train(:,12:12));
model = stepwiselm(x_train, y_train, 'linear');

mkdir('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise/');

terms = model.Coefficients.Row;
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise/terms.txt', 'w');
fprintf(fileID, '%s\n', terms{:})
fclose(fileID);

coefs = model.Coefficients.Estimate;
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise/coefs.txt', 'w');
fprintf(fileID, '%10.2f\n', coefs)
fclose(fileID);

pValues = model.Coefficients.pValue
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise/pValues.txt', 'w');
fprintf(fileID, '%3.2f\n', pValues)
fclose(fileID);