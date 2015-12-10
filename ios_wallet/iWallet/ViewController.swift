import UIKit
import Geth

class ViewController: UIViewController {
    @IBOutlet weak var webview: UIWebView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Start up a geth instance with RPC enabled and a permissive CORS
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            Geth.run("--ipcdisable --fast --rpc --rpccorsdomain *")
        }
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0)) {
            let wallet = NSBundle.mainBundle().URLForResource("index", withExtension: "html", subdirectory: "wallet")
            let request = NSURLRequest(URL: wallet!)	
            self.webview.loadRequest(request)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
