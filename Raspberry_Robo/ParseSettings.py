import configparser
import os
 
path = "settings.ini" 
 
def create_config(path):
    """
    Create a config file
    """
    config = configparser.ConfigParser()
    config.add_section("Settings")
    config.set("Settings", "Face recognition", "1")
    config.set("Settings", "Subject monitoring", "1")
    config.set("Settings", "Sound", "1")
    config.set("Settings", "Mic", "1")
    config.set("Settings", "Operation mode", "0")
    config.set("Settings", "Battery", "100")
    config.add_section("PIDS")
    config.set("PIDS", "Interaction", "0")
    config.set("PIDS", "Sound", "0")
    
    with open(path, "w") as config_file:
        config.write(config_file)
 
 
def get_config(path):
    """
    Returns the config object
    """
    if not os.path.exists(path):
        create_config(path)
    
    config = configparser.ConfigParser()
    config.read(path)
    return config
 
 
def get_setting_str():
    """
    Print out a setting
    """  
    config = get_config(path)
    value = config.get("Settings", "Sound")
    msg = "Звук установлен в {value}".format(
        value=value
    )
    value = config.get("Settings", "Mic")
    msg = msg + "Микрофон установлен в {value}".format(
        value=value
    )
    value = config.get("Settings", "Face recognition")
    msg = msg + "Распознавание установлено в {value}".format(
        value=value
    )

    value = config.get("Settings", "Subject monitoring")
    msg = msg + "Мониторинг установлен в {value}".format(
        value=value
    )
    
    value = config.get("Settings", "Operation mode")
    msg = msg + "Режим  установлен в {value}".format(
        value=value
    )
    print(msg)
    return msg

def get_setting(path, section, setting):
    """
    Print out a setting
    """
    config = get_config(path)
    value = config.get(section, setting)
    msg = "{section} {setting} is {value}".format(
        section=section, setting=setting, value=value
    )
    
    print(msg)
    return value
 
 
def update_setting(path, section, setting, value):
    config = get_config(path)
    config.set(section, setting, value)
    with open(path, "w") as config_file:
        config.write(config_file)
 
 
def delete_setting(path, section, setting):
    config = get_config(path)
    config.remove_option(section, setting)
    with open(path, "w") as config_file:
        config.write(config_file)
 
 
if __name__ == "__main__":
    path = "settings.ini"
