# Todo 

一个命令行的 Todo 待办事项软件。


构建产物的步骤：
1. 在 build.gradle 中修改 version 对应的版本号; sync 代码；
2. 执行 ./build.gradle 中的 jar 任务；
3. 产物会生成在 local.properties 中指定的 ${build.dir}/libs/ 目录中; 