[![Build Status](https://travis-ci.org/tsmarsh/stl-clj.svg?branch=master)](https://travis-ci.org/tsmarsh/stl-clj) 

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

The primary use case for this library is taking a directory of Binary STL files and turning them into a single STL file to reduce the need for baby sitting.

The machine is just the printable area defined as a width, depth, height in millimeters: [w d h]

The buffer is added as a border to each file to prevent them touching, values between 1.0 and 5.0 have worked well for me.

Output is the absolute path to the destination file.

Dir is directory containing the stl files.


```
(combine [75.0 75.0 75.0] 0.0 "/path/to/output.stl" "/path/to/input/stl")
```

It is possible that more convienience functions like this will emerge
as more real world use happens.

Whilst it is trivial to use this now if you are a clojure developer, the primary function of this library is to back an application with a more humane interface.

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
