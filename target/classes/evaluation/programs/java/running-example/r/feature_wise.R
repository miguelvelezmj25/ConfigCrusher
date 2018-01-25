feature_wise <- read.csv("src/main/resources/evaluation/programs/java/running-example/r/feature_wise.csv")
model <- lm(time~A+B+C+D, data = feature_wise)
coef(model)
