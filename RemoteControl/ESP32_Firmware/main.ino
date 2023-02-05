#include <ESPManager.h>

ESPManager manager = ESPManager();


void setup() {
  manager.begin();
}

void loop() {
  manager.handle();
}
