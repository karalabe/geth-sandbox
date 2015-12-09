import UIKit
import Geth

class ViewController: UIViewController {
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var progress: UIProgressView!
    
    @IBAction func start(sender: UIButton) {
        NSTimer.scheduledTimerWithTimeInterval(1.0, target: self, selector: Selector("update"), userInfo: nil, repeats: true)
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            Geth.run("--ipcdisable --fast --rpc")
        }
    }
    
    func update() {
        let req = NSMutableURLRequest(URL: NSURL(string: "http://127.0.0.1:8545")!)
        req.HTTPMethod = "POST"
        req.HTTPBody = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_syncing\",\"params\":[],\"id\":1}".dataUsingEncoding(NSUTF8StringEncoding)
        
        var res: NSURLResponse?
        do {
            let data = try NSURLConnection.sendSynchronousRequest(req, returningResponse: &res)
            let reply = try NSJSONSerialization.JSONObjectWithData(data, options: NSJSONReadingOptions.MutableContainers) as! NSDictionary
            if reply["result"] is Bool {
                label.text = "Geth not syncing..."
            } else {
                let startingStr = (reply["result"] as! NSDictionary)["startingBlock"] as! String
                let startingHex = startingStr.substringFromIndex(startingStr.startIndex.advancedBy(2))
                let startingInt = Int(startingHex, radix: 16)!
                
                let currentStr = (reply["result"] as! NSDictionary)["currentBlock"] as! String
                let currentHex = currentStr.substringFromIndex(currentStr.startIndex.advancedBy(2))
                let currentInt = Int(currentHex, radix: 16)!
                
                let highestStr = (reply["result"] as! NSDictionary)["highestBlock"] as! String
                let highestHex = highestStr.substringFromIndex(highestStr.startIndex.advancedBy(2))
                let highestInt = Int(highestHex, radix: 16)!
                
                label.text = "Processing block #" + String(currentInt)
                progress.setProgress(Float(currentInt - startingInt) / Float(highestInt - startingInt), animated: true)
            }
        } catch (_) {}
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

