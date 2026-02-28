#!UTF-8
import re
import json
import subprocess
from datetime import datetime
from pathlib import Path


def get_current_version(build_gradle_path: str) -> str:
    with open(build_gradle_path, 'r', encoding='utf-8') as f:
        content = f.read()
    match = re.search(r"version\s*=\s*'(\d+\.\d+\.\d+)'", content)
    if not match:
        raise ValueError("Cannot find version in build.gradle")
    return match.group(1)


def increment_version(version: str) -> str:
    parts = version.split('.')
    parts[2] = str(int(parts[2]) + 1)
    return '.'.join(parts)


def calculate_version_code(version: str) -> int:
    parts = version.split('.')
    return int(parts[0]) * 10000 + int(parts[1]) * 100 + int(parts[2])


def update_build_gradle(build_gradle_path: str, new_version: str):
    with open(build_gradle_path, 'r', encoding='utf-8') as f:
        content = f.read()
    new_content = re.sub(
        r"version\s*=\s*'\d+\.\d+\.\d+'",
        f"version = '{new_version}'",
        content
    )
    with open(build_gradle_path, 'w', encoding='utf-8') as f:
        f.write(new_content)


def run_build():
    result = subprocess.run(
        [f'gradlew', 'jar'],
        shell=True,
        cwd=Path(__file__).parent
    )
    if result.returncode != 0:
        raise RuntimeError(f"Build failed with return code {result.returncode}")


def generate_version_json(output_path: str, version: str, version_code: int, release_notes: str):
    version_info = {
        "version": version,
        "versionCode": version_code,
        "releaseDate": datetime.now().strftime("%Y-%m-%d"),
        "downloadUrl": "",
        "releaseNotes": release_notes
    }
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(version_info, f, indent=2, ensure_ascii=False)


def main():
    script_dir = Path(__file__).parent
    build_gradle_path = script_dir / 'build.gradle'
    version_json_path = script_dir / '.documents' / 'latest_version.json'
    
    print("Reading current version from build.gradle...")
    current_version = get_current_version(str(build_gradle_path))
    print(f"Current version: {current_version}")
    
    new_version = increment_version(current_version)
    print(f"New version: {new_version}")
    
    release_notes = input("Please enter release notes: ").strip()
    if not release_notes:
        print("Release notes cannot be empty!")
        return
    
    print(f"Updating build.gradle to version {new_version}...")
    update_build_gradle(str(build_gradle_path), new_version)
    
    print("Running build...")
    run_build()
    
    print("Generating latest_version.json...")
    version_code = calculate_version_code(new_version)
    version_json_path.parent.mkdir(parents=True, exist_ok=True)
    generate_version_json(str(version_json_path), new_version, version_code, release_notes)
    
    print(f"Done! Version {new_version} has been published.")


if __name__ == '__main__':
    main()
