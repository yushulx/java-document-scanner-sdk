# Java Document Scanner SDK for Windows and Linux
The repository is a Java wrapper for [Dynamsoft Document Normalizer](https://www.dynamsoft.com/document-normalizer/docs/introduction/?ver=latest). It supports document edge detection and document normalization. 

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

### Linux 

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

## Test the Jar Package for Document Scanning

```
java -cp target/ddn-1.0.0.jar com.dynamsoft.ddn.Test images/sample-image.png <optional: template.json> <optional: license key>
```

![Java document scanner: document edge detection and normalization](https://www.dynamsoft.com/codepool/img/2022/11/java-document-scanner-edge-detection-normalization.png)
