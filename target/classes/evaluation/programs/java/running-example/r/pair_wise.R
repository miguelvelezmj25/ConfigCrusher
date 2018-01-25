pair_wise <- read.csv("src/main/resources/evaluation/programs/java/running-example/r/pair_wise.csv")
model <- lm(time~A+B+C+D+A*B+A*C+B*C+A*D+B*D+C*D+A*B*C+A*B*D+A*C*D+B*C*D+A*B*C*D, data = pair_wise)
coef(model)
