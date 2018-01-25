feature_wise <- read.csv("src/main/resources/evaluation/programs/java/pngtasticOptimizer/r/feature_wise.csv")
model <- lm(time~COMPRESSOR+ITERATIONS+LOGLEVEL+COMPRESSIONLEVEL+REMOVEGAMMA, data = feature_wise)
coef(model)
