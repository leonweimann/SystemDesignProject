// (c) Daniel Fertmann, Uni Freiburg, 2023

// pack the extension with 'vsce package'

// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require('vscode');

//Output channel for displaying errors from compiler
var outputChannel = vscode.window.createOutputChannel("Compiling Errors");

// Calling executables
const util = require('util');
const execFileAsync = util.promisify(require('child_process').execFile);
const execAsync = util.promisify(require('child_process').exec);
var execFile = require('child_process').execFile;
var exec = require('child_process').exec;
var path = require('path');
//const { spawn } = require('child_process');
// const fs = require('fs');  // if (fs.existsSync(path)) {} else {}

/**
 * @param {vscode.ExtensionContext} context
 */
function activate(context) {
	// This line of code will only be executed once when your extension is activated

	// What operating System are we running?
	var OS = process.platform;
	var Windows = false;
	var MacOS = false;
	var Linux = false;
	var compilerPath;
	var pathToFantom = path.join(__dirname, 'files', 'NXT_Fantom_Drivers_v120.zip');

	// @ts-ignore
	if (OS == "win32" || OS == "win64") {
		compilerPath = path.join(__dirname, 'files', 'Windows', 'nbc.exe');
    	Windows = true;		
		// install_fantom_driver();  // Check if LEGOs FANTOM-Driver is installed
	} else if (OS == "darwin") {
		compilerPath = "./" + path.join(__dirname, 'files', 'Mac', 'nbc');
    	MacOS = true;
	} else if (OS == "linux") {
		compilerPath = path.join(__dirname, 'files', 'Linux', 'nbc');  //compilerPath = "nbc";
    	Linux = true;
	}


	let disposable = vscode.commands.registerCommand('nxt-compiler.sendToNXT', function () {
		// The code you place here will be executed every time your command is executed
		send_to_nxt('-d');
	});
	let disposable2 = vscode.commands.registerCommand('nxt-compiler.sendToNXTAndRun', function () {
		// The code you place here will be executed every time your command is executed
		send_to_nxt('-r');
	});
	let disposable3 = vscode.commands.registerCommand('nxt-compiler.installFantomDriver', function () {
		// The code you place here will be executed every time your command is executed
		install_fantom_driver();
	});
	

	async function install_fantom_driver() {
		try {
			const { stdout, stderr } = await execFileAsync('reg', ['query', 'HKEY_LOCAL_MACHINE\\Software\\Classes\\Installer\\Features\\82C1380AAF6A3A9468EA5BEAC3E91EC9']);
			if (stdout) {  // Fantom Driver still installed
			  outputChannel.appendLine('Fantom Driver is installed');
			  outputChannel.show();

			  return true;
			}
		}
		catch {  // Fantom Driver is not installed -> Install it
			const batchScriptPath = path.join(__dirname, 'files/installFantomDriver.bat');
			const args = [pathToFantom, path.dirname(pathToFantom)];
			await executeCommandInTerminal(`"${batchScriptPath}" ${args.join(' ')}`);

			return false;
		}
	}
	
	async function executeCommandInTerminal(command) {
		try {
			// Create a new terminal or reuse an existing one
			const terminal = vscode.window.terminals.find((t) => t.name === 'Fantom Driver Installation') || vscode.window.createTerminal('Fantom Driver Installation',	process.env.COMSPEC);
	
			// Run the command in the terminal
			terminal.show();
			terminal.sendText(command);
	
			// Wait for a specific amount of time (adjust as needed)
			await sleep(5000);
	
			// You can capture and process the terminal output as needed
		} catch (error) {
			throw error;
		}
	}

	
	function sleep(ms) {
		return new Promise((resolve) => setTimeout(resolve, ms));
	}
	

	async function send_to_nxt(parameterForCompiler) {
		/** Saves the actual focused file, compiles it and send it to the NXt. If parameter = 'd' it 
		 *  will only send it to NXT. With parameter = 'r' it will also run the program on NXT after
		 *  sending
		 */

		outputChannel.clear();
		// Check if Fantom Driver is installed and install it
		if(Windows && ! install_fantom_driver()){
			return;
		}

		// Save currently open file and get its filename
		const editor = vscode.window.activeTextEditor;
        if (! editor) {
			vscode.window.showErrorMessage('Upload failed: Open a file');
			return;
		}
		
        let document = editor.document;
        const documentText = await document.save();

		var filename = document.uri.path;

		if (filename.startsWith('extension')) {  // If output is focused, filemame is not correct
			vscode.window.showErrorMessage('Upload failed: Focus a file');
			return;
		}

		if (Windows) {  // Correction for Windows filenames needed
			var filename = filename.substring(1);
		}

		var file2 = path.join(filename);  // Corrects the / to \
		var args = [parameterForCompiler, file2];  // -r for download and run, -d for only download
		console.log(compilerPath + " " + args[0] + " " + args[1]);
		execFile(compilerPath, args, function(err, data) {
			if (err) {
				if (err.message.split('\n').length ==2) { // For windows
					vscode.window.showErrorMessage('Upload failed: Check connection and turn on the NXT');
				}
				else if (err.message.substring(0, 5) == "spawn"){  // For Linux
					vscode.window.showErrorMessage('Upload failed: Check connection and turn on the NXT');
				}
				//libusb-0.1.so.4: cannot open shared object file: No such file or directory
				else if (err.message.includes("libusb-0.1.so.4")) {
					// Create a new terminal window
					const terminal = vscode.window.createTerminal('Install libusb-0.1-4:i386');
					terminal.sendText('sudo apt-get install libusb-0.1-4:i386');
					terminal.show();

					vscode.window.showInformationMessage("'libusb-0.1-4:i386' needs to be installed. Enter your admin password to the terminal below");
				}
				else {
					vscode.window.showErrorMessage('Upload failed: Check your code for errors');
					outputChannel.appendLine(err.message);
					outputChannel.show();
				}
				//vscode.window.showInformationMessage('Unidentified error occurred');
				//outputChannel.appendLine(err);
				//outputChannel.show();
				console.error('exec error: ');
				console.error(err);
				return;
			}
			vscode.window.showInformationMessage('Sucessfully uploaded');
			console.log(data.toString());
		});
	}
}

// This method is called when your extension is deactivated
function deactivate() {}

module.exports = {
	activate,
	deactivate
}


