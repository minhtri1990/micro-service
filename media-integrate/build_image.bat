mvn clean install && docker build -t parking-media-integrate:0.2.0 . && docker save --output parking-media-integrate-0.2.tar parking-media-integrate:0.2.0 