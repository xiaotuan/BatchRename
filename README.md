# BatchRename

`BatchRename` 程序用于将文件夹中一级目录下的所有文件重命名为指定格式的文件名。

> 注意：`BatchRename` 程序通过文件名中最后一个 `.` 字符来分割文件名和文件后缀名，如果要重命名的文件后缀名有多个 `.` 字符，则会导致重命名后缀出现错误。例如：`Linux` 系统中的 `test.tar.gz` 文件，重命名后将可能变成 `test.gz`。
因此该程序只针对文件后缀名中只有一个 `.` 的文件。例如：`text.png`。

# 使用方法

`BatchRename` 需要使用 `-P` 或 `--path` 参数指定重命名文件所在的文件夹路径，例如（这里假设 `BatchRename.exe` 程序位于当前目录命令行环境下，以下相同）：

```shell
$ ./BatchRename.exe -P C:/testdir
```

> 注意：这里没有指定文件名前缀和文件编号，重命名文件时，文件编号起始值为 0，没有前缀字符。

如果希望重命名后给每个文件添加前缀，可以使用 `-p` 或 `-prefix` 参数指定文件名前缀，例如：

```shell
$ ./BatchRename.exe -P C:/testdir -p animation_
```

如果希望指定文件的起始编号，可以使用 `-n` 或 `--number` 参数指定文件起始编号，例如：

```shell
$ ./BatchRename.exe -P C:/testdir -p animation_ -n 100
```

> 注意：文件起始编号必须是整数。

也可以直接运行 `BatchRename` 程序或使用 `-h` 或 `--help` 参数来查看 `BatchRename` 程序的使用说明，例如：

```shell
$ BatchRename.exe

使用方法:
    -P / --path <path> 指定需要重命名文件所在的文件夹路径
    -p / --prefix <string> 指定重命名文件名前缀
    -n / --number <int> 指定重命名起始编号

```