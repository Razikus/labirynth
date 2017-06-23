#!/bin/bash

rm -rf dist

ant

rm -rf build

cp config.xml dist/
