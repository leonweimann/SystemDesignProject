import os

def delete_class_files():
    """Deletes all .class files in the current directory and its subdirectories."""
    base_path = os.path.dirname(os.path.abspath(__file__))  # Get the script's directory

    # Walk through the directory and subdirectories
    for root, dirs, files in os.walk(base_path):
        for file in files:
            if file.endswith(".class"):  # Check if the file is a .class file
                file_path = os.path.join(root, file)
                try:
                    os.remove(file_path)  # Delete the file
                    print(f"Deleted: {file_path}")
                except Exception as e:
                    print(f"Error deleting {file_path}: {e}")

if __name__ == "__main__":
    delete_class_files()
