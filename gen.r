#!/usr/local/bin/Rscript

library("GA")
library("proxy")
library("MASS")

#Lee una proyección y deduce el número de clusters de ella

args <- commandArgs(TRUE)
files <- read.table(args[1],sep=";",head=FALSE)
dimen <- length(files[,1])

minv = rep(0,dimen)
maxv = rep(1,dimen)

fitness <- function(population)
{
	arguments = paste(as.character(files[which(as.logical(population), arr.ind = TRUE, useNames = TRUE),1]),collapse=' ')
	print(arguments)
	system(paste("sh fitnessFunctio.sh",arguments,sep=" "))
	val <- read.table("sol.txt",sep=",",head=FALSE)
	val[1,1]
}
GA <- ga(type="binary",fitness=fitness,nBits=dimen,popSize=2)
print(GA)
