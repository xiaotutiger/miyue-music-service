all:help
jar:
	mvn assembly:assembly
run:	
	nohup java -jar target/miyue-netty-service-0.0.1-SNAPSHOT-executable.jar &
clean:
	rm -rf target
pid:
	ps aux|grep miyue-netty-service |awk '{print $2}'
see:
	tail -f nohup.out
help:
	@echo "Usage:"
	@echo " make jar     - Build Jar File."
	@echo " make clean   - Clean up Output directory on HDFS."
	@echo " make run     - Run your MapReduce code on Hadoop."
	@echo " "
	@echo "Example:"
	@echo " make jar; make run; make output; make clean"
