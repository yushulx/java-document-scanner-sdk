cmake_minimum_required (VERSION 2.6)
project (ddn)
MESSAGE( STATUS "PROJECT_NAME: " ${PROJECT_NAME} )

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

MESSAGE( STATUS "JAVA_INCLUDE: " ${JAVA_INCLUDE})

# Check lib
if (CMAKE_HOST_WIN32)
    set(WINDOWS 1)
elseif(CMAKE_HOST_APPLE)
    set(MACOS 1)
elseif(CMAKE_HOST_UNIX)
    set(LINUX 1)
endif()

# Set RPATH
if(CMAKE_HOST_UNIX)
    SET(CMAKE_CXX_FLAGS "-std=c++11 -O3 -Wl,-rpath=$ORIGIN")
    SET(CMAKE_INSTALL_RPATH "$ORIGIN")
    SET(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)
endif()

# Add search path for include and lib files
if(WINDOWS)
    link_directories("${PROJECT_SOURCE_DIR}/lib/win/" ${JNI_LIBRARIES}) 
elseif(LINUX)
    link_directories("${PROJECT_SOURCE_DIR}/lib/linux/" ${JNI_LIBRARIES})
endif()
include_directories("${PROJECT_BINARY_DIR}" "${PROJECT_SOURCE_DIR}/include/")


# Add the library
add_library(ddn SHARED NativeDocumentScanner.cxx)
if(WINDOWS)
    target_link_libraries (${PROJECT_NAME} "DynamsoftCorex64" "DynamsoftDocumentNormalizerx64")
else()
    target_link_libraries (${PROJECT_NAME} "DynamsoftCore" "DynamsoftDocumentNormalizer" pthread)
endif()

# if(WINDOWS)
#     add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD 
#     COMMAND ${CMAKE_COMMAND} -E copy_directory
#     "${PROJECT_SOURCE_DIR}/lib/win/"      
#     $<TARGET_FILE_DIR:ddn>)
# endif()

# Set installation directory
set(CMAKE_INSTALL_PREFIX "${PROJECT_SOURCE_DIR}/../src/main/")
set(ECLIPSE_PATH "java/com/dynamsoft/ddn/native")
set(MAVEN_PATH "resources/com/dynamsoft/ddn/native")
if(WINDOWS)
    install (DIRECTORY "${PROJECT_SOURCE_DIR}/lib/win/" DESTINATION "${CMAKE_INSTALL_PREFIX}${ECLIPSE_PATH}/win")
    install (DIRECTORY "${PROJECT_SOURCE_DIR}/lib/win/" DESTINATION "${CMAKE_INSTALL_PREFIX}${MAVEN_PATH}/win")
    install (TARGETS ddn DESTINATION "${CMAKE_INSTALL_PREFIX}${ECLIPSE_PATH}/win")
    install (TARGETS ddn DESTINATION "${CMAKE_INSTALL_PREFIX}${MAVEN_PATH}/win")
elseif(LINUX)
    install (DIRECTORY "${PROJECT_SOURCE_DIR}/lib/linux/" DESTINATION "${CMAKE_INSTALL_PREFIX}${ECLIPSE_PATH}/linux")
    install (DIRECTORY "${PROJECT_SOURCE_DIR}/lib/linux/" DESTINATION "${CMAKE_INSTALL_PREFIX}${MAVEN_PATH}/linux")
    install (TARGETS ddn DESTINATION "${CMAKE_INSTALL_PREFIX}${ECLIPSE_PATH}/linux")
    install (TARGETS ddn DESTINATION "${CMAKE_INSTALL_PREFIX}${MAVEN_PATH}/linux")
endif()



