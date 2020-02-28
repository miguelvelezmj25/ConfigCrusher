filename = '/Users/mvelezce/Documents/programming/java/projects/ConfigCrusher/cc-eval/src/main/resources/eval/java/programs/sampling/profiler/jprofiler/MeasureDiskOrderedScan/real/MeasureDiskOrderedScan.csv';
delimiter = ',';
startRow = 2;
endRow = 292;
formatSpec = '%*q%f%f%*s%*s%[^\n\r]';

fileID = fopen(filename,'r');
textscan(fileID, '%[^\n\r]', startRow-1, 'WhiteSpace', '', 'ReturnOnError', false);
dataArray = textscan(fileID, formatSpec, endRow-startRow+1, 'Delimiter', delimiter, 'TextType', 'string', 'ReturnOnError', false, 'EndOfLine', '\r\n');
fclose(fileID);

y = dataArray{:, 1};
x = dataArray{:, 2};

clearvars filename delimiter startRow endRow formatSpec fileID dataArray ans;

X = [ones(length(x),1) x];
interceptAndSlope = X\y;
interceptAndSlope

res = X*interceptAndSlope;
Rsq2 = 1 - sum((y - res).^2)/sum((y - mean(y)).^2)
