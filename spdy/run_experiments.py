import os
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import unicodedata
import time

latency = [0.0, 5.0, 10.0, 20.0, 30.0, 40.0]
packet_loss = [0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 1.0, 2.0, 3.0, 5.0, 10.0]
experiment_name="exp"
folder = "SPDY_Validation"
NUM_EXPERIMENTS=5
anue_browser = webdriver.Firefox()
http = open("http-3d.txt", "w")
spdy = open("spdy-3d.txt", "w")
debug = open("debug_log", "w")
http_csv = open("http.csv", "w")
spdy_csv = open("spdy.csv", "w")


def run_experiment(latency, packet_loss, is_spdy_enabled):
    global results
    global debug
    
    #turn spdy on
    toggle_spdy(is_spdy_enabled)
    
    #anue adds up latency on the blades, have to divide by 2 and set on each
    if latency != 0.0:
        latency = latency/2
    
    #change the latency and packet loss based on the experiment parameters
    change_latency(latency)
    change_packet_loss(packet_loss)
    
    if is_spdy_enabled:
        dir_name = "http/" + experiment_name + '_' + str(latency*2) + '_' + str(packet_loss)
    else:
        dir_name = "spdy/" + experiment_name + '_' + str(latency*2) + '_' + str(packet_loss)
    
    #get final results
    load_time_results = load_page(dir_name, is_spdy_enabled)
    
    for item in load_time_results:
        log(is_spdy_enabled, str(item) + " ")
        print str(item) + " "
    log(is_spdy_enabled, "\n")

def log(is_spdy_enabled, message):
    global http
    global spdy
    if is_spdy_enabled:
        spdy.write(message)
    else:
        http.write(message)
        
def change_latency(value):
    global debug
    profile = '0'
    blade = '1'
    latency = str(value)
    anue_address = 'anue'
    for i in range(0,2):
        try:
            anue_browser.get('http://' + anue_address + '/gem_profile_impairment.htm?blade=' + blade + '&profile=' + profile)
            latency_input = anue_browser.find_element_by_id('htmlBasicJitterDAvg')

            #javascript in field returns NaN when clear() is used.
            clear_input(latency_input)
        
            #send latency value to the anue and submit
            latency_input.send_keys(str(value))
            anue_browser.find_element_by_id('appButton').click()
        except:
            print "WARNING: Could not set latency to value " + str(value) + " (okay for 0 latency tests)"
            debug.write("WARNING: Could not set latency to value " + str(value) + " (okay for 0 latency tests)\n")

        #submit form and close browser
        blade = '3'
        
def change_packet_loss(value):
    global debug
    profile = '0'
    blade = '1'
    anue_address = 'anue'
    packet_loss = value
    for i in range(0,2):
        try:
            anue_browser.get('http://' + anue_address + '/gem_profile_impairment.htm?blade=' + blade + '&profile=' + profile)
        
            packet_loss_val1 = 0
            packet_loss_val2 = 0
        
            if value == 0.0:
                if anue_browser.find_element_by_id("htmlImpDropEnabled").is_selected():
                    anue_browser.find_element_by_id("htmlImpDropEnabled").click();
                anue_browser.find_element_by_id("appButton").click()
            else:
                packet_loss_val1 = "1"
                packet_loss_val2 = str(int(100 / value))
                anue_browser.find_element_by_id("htmlImpDropVisBut").click()
                packet_loss_1 = anue_browser.find_element_by_name("htmlPdropBurstLen")
                clear_input(packet_loss_1)
                packet_loss_1.send_keys(packet_loss_val1)
        
                packet_loss_2 = anue_browser.find_element_by_name("htmlPdropInterval")
                clear_input(packet_loss_2)
                packet_loss_2.send_keys(packet_loss_val2)

                #submit form and close browser
                anue_browser.find_element_by_id("appButton").click()
        except:
            print "WARNING: Error setting packet loss to " + str(value)
            debug.write("WARNING: Error setting packet loss to " + str(value))
        blade = '3'

def toggle_spdy(is_spdy_enabled):
    if(is_spdy_enabled):
        os.system("ssh root@wind \"sed -i 's/SpdyEnabled off/SpdyEnabled on/' /etc/apache2/conf.d/spdy.conf\"")
        os.system("ssh root@wind 'systemctl restart apache2.service'")
        time.sleep(5)
    else:
        os.system("ssh root@wind \"sed -i 's/SpdyEnabled on/SpdyEnabled off/' /etc/apache2/conf.d/spdy.conf\"")
        os.system("ssh root@wind 'systemctl restart apache2.service'")
        time.sleep(5)
        
def clear_input(html_element):
    html_element.click()
    html_element.send_keys(Keys.COMMAND, Keys.ARROW_RIGHT)
    while html_element.get_attribute('value') != '':
        html_element.send_keys(Keys.BACKSPACE)    
        
def reset_stats():
    #for i in range(1,4):
    browser = webdriver.Firefox()
    browser.get("http://anue.iol.unh.edu/gem_profile_stats.htm?blade=3&profile=0")
    browser.find_element_by_xpath("//input[@value='Reset All Blade Stats']").click()
    try:
        WebDriverWait(browser, 5).until(EC.alert_is_present(),
                                       'Timed out waiting for ' +
                                       'confirmation popup to appear.')

        alert = browser.switch_to_alert()
        alert.accept()
    except TimeoutException:
        print "no alert showed up!"
    element = WebDriverWait(browser, 10).until(lambda browser: browser.find_element_by_id('htmlRxTotalBytes'))
    btrans = element.get_attribute('value')
    if btrans != "0":
        browser.quit()
        reset_stats()
    else:
        browser.quit()

#retval is -1 if error occured
def get_bytes_transferred():
    anue_browser.get("http://anue.iol.unh.edu/gem_profile_stats.htm?blade=3&profile=0")
    time.sleep(7)    
    element = anue_browser.find_element_by_id('htmlRxTotalBytes')
    btrans = element.get_attribute('value')
    str_btrans = btrans.encode('ascii','ignore')
    retval = float(str_btrans.replace(',',''))
    return retval
    
def load_page(result_directory, is_spdy_enabled):
    global debug
    #results are in (loadtime, bytes_transferred throughput format)
    loadtimes = []
    transfers = []
    throughputs = []
    
    #create folder where pcaps will be stored
    os.system("ssh -t root@athena mkdir /usr/local/bin/spdy_tests/" + folder + "/" + 
        result_directory)
    
    for i in range(NUM_EXPERIMENTS):
        #reset blade stats for bytes transferred
        reset_stats()
        
        #load page and start capture
        browser = webdriver.Chrome()
        os.system("ssh root@athena nohup tcpdump -U -B8192 -n ip -i eth2 -w /usr/local/bin/spdy_tests/" + 
            folder + "/" + result_directory + "/experiment" + str(i) + ".pcap& sleep 2")
        t0 = time.time()
        browser.get('https://10.10.10.20/index.php')
        loadtime = time.time() - t0
        loadtimes.append(loadtime)
        os.system("ssh -t root@athena 'pkill tcpdump'")
        
        #Make sure btrans got cleared correctly
        btrans = get_bytes_transferred()
        transfers.append(btrans)
        
        #add throughput
        throughputs.append(btrans / loadtime)
        
        if is_spdy_enabled:
            spdy_csv.write(str(loadtime) + "," + str(btrans) + "\n")
        else:
            http_csv.write(str(loadtime) + "," + str(btrans) + "\n")
        browser.quit()
    return calculate_results(loadtimes, transfers, throughputs)
            
def calculate_results(loadtimes, transfers, throughputs):
    final_results = []
    lsum = 0.0
    tsum = 0.0
    thsum = 0.0
    for l in loadtimes:
        lsum = lsum + l
    final_results.append(lsum / NUM_EXPERIMENTS)
    for t in transfers:
        tsum = tsum + t
    final_results.append(tsum / NUM_EXPERIMENTS)
    for th in throughputs:
        thsum = thsum + th
    final_results.append(thsum / NUM_EXPERIMENTS)
    return final_results
    
if __name__ == "__main__":
    #create directory folder
    os.system("ssh -t root@athena mkdir /usr/local/bin/spdy_tests/" + folder)
    os.system("ssh -t root@athena mkdir /usr/local/bin/spdy_tests/" + folder + "/http")
    os.system("ssh -t root@athena mkdir /usr/local/bin/spdy_tests/" + folder + "/spdy")
    http.truncate()
    spdy.truncate()
    http.write("#HTTP Automator v. 0.8\n")
    http.write("#Date: 10/16/14\n")
    spdy.write("#HTTP Automator v. 0.8\n")
    spdy.write("#Date: 10/16/14\n")
    for lval in latency:
        log(0, "\n")
        log(1, "\n")
        for pval in packet_loss:
            log(0, str(pval) + " " + str(lval) + " ")
            run_experiment(lval, pval, 0)
            log(1, str(pval) + " " + str(lval) + " ")
            run_experiment(lval, pval, 1)
    #make graph
    os.system("gnuplot plot.gnuplot")
    os.system("scp *.log *.txt *.pdf root@athena:/usr/local/bin/spdy_tests/")
    anue_browser.quit()
