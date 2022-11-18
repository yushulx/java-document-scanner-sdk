# Java Document Scanner SDK for Windows and Linux
The repository is a Java wrapper for [Dynamsoft Document Normalizer](https://www.dynamsoft.com/barcode-reader/overview/). It supports document edge detection and document normalization. 

## License Key
Apply for a [30-day free trial license](https://www.dynamsoft.com/customer/license/trialLicense/?product=ddn).

## JNI Header Generation

```bash
cd src/main/java
javah -o ../../../jni/NativeDocumentScanner.h com.dynamsoft.ddn.NativeDocumentScanner
```

## Build the JNI Shared Library with CMake

### Windows
```
mkdir build
cd build
cmake -DCMAKE_GENERATOR_PLATFORM=x64 ..
cmake --build . --config Release --target install
```

### Linux & macOS

```
mkdir build
cd build
cmake .. 
cmake --build . --config Release --target install
```

## Build the Jar Package Using Maven

```
mvn package
```

## Test the Jar Package for Barcode Reading

```
java -cp target/barcode-1.0.0.jar com.dynamsoft.ddn.Test <image file>
```

