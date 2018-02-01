% feature_wise

train = readtable('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise.csv');
x_train = table2array(train(:,1:4));
y_train = table2array(train(:,5:5));
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

formula = model.Formula
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/feature_wise/formula.txt', 'w');
fprintf(fileID, '%s\n', formula)
fclose(fileID);

% pair_wise

train = readtable('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise.csv');
x_train = table2array(train(:,1:4));
y_train = table2array(train(:,5:5));
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

formula = model.Formula
fileID = fopen('../../../../../../../../resources/evaluation/programs/java/running-example/data/pair_wise/formula.txt', 'w');
fprintf(fileID, '%s\n', formula)
fclose(fileID);