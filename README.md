# stl-clj

A Clojure library designed aid the manipulation of STL files.

Ultimate aim is to make it trivial to collect and flatten multiple
stls into a single stl file.

Current status:

* Read binary stl
* Write binary stl
* Linear translation of stls
* Combination of multiple stl into a single file

Planning on creating an app that simplifies the process using this
library.

## Usage

This code takes filenames as input, reads in the stl files,
distributes them across the x axis with a 10 gap, then combines them
into a single file.

```
(defn combine-files [output & filenames]
  (let [stls (map r/read-stl filenames)
        faces (map t/facify stls)
        distributed-faces (t/distribute-x faces 10)
        recombined-faces (t/combine distributed-faces)
        normalized-faces (t/normalize recombined-faces)]
    (w/write-stl normalized-faces output)))
```

It is possible that more convienience functions like this will emerge
as more real world use happens.

## License
Copyright © 2015 T S MARSH

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
