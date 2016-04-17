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
	if(sum(population)>900)
	{
		0
	}
	else
	{
		arguments = paste(as.character(files[which(as.logical(population), arr.ind = TRUE, useNames = TRUE),1]),collapse=' ')
		print(arguments)
		time <- system.time(system(paste("sh fitnessFunctio.sh",arguments,sep=" ")))
		val <- read.table("sol.txt",sep=",",head=FALSE)
		print(paste("Solution = ",val[1,1],sum(population),time[[1]],sep=" "))
		1.5*val[1,1]-0.5*sum(population)/length(files[,1])-time[[1]]/40
	}
}
GA <- ga(type="binary",fitness=fitness,nBits=dimen,popSize=5)
print(GA)
