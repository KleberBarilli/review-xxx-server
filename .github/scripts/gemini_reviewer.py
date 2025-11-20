import os
import sys
import google.generativeai as genai
from github import Github

MAX_PATCH_LENGTH = 10000
VALID_EXTENSIONS = [
    '.js', '.jsx', '.ts', '.tsx',
    '.java', '.kt', '.scala',
    '.py',
    '.go',
    '.rs',
    '.cs',
    '.c', '.cpp', '.h',
    '.php',
    '.rb',
    '.swift',
    '.html', '.css', '.scss',
    '.sql',
    '.yml', '.yaml', '.json'
]

def main():
    # Gemini API Configuration
    try:
        api_key = os.environ['GEMINI_API_KEY']
        genai.configure(api_key=api_key)
    except KeyError:
        print("❌ Error: GEMINI_API_KEY not found in environment variables.")
        sys.exit(1)

    # Model Selection (Smart Fallback)
    try:
        model = genai.GenerativeModel('models/gemini-2.5-pro')
    except Exception:
        print("⚠️ Warning: Gemini 2.5 Pro model not found, attempting fallback to 1.5 Pro...")
        model = genai.GenerativeModel('gemini-1.5-pro-latest')

    # GitHub Connection
    try:
        token = os.environ['GITHUB_TOKEN']
        repo_name = os.environ['REPO_NAME']
        pr_number = int(os.environ['PR_NUMBER'])

        g = Github(token)
        repo = g.get_repo(repo_name)
        pr = repo.get_pull(pr_number)
    except KeyError as e:
        print(f"❌ GitHub Action configuration error: Missing variable {e}.")
        sys.exit(1)

    print(f'🔍 Analyzing PR #{pr_number} in repo {repo_name}...')

    code_diffs = ''
    files_analyzed = 0

    # Iterate over PR files
    for file in pr.get_files():
        _, ext = os.path.splitext(file.filename)

        if ext in VALID_EXTENSIONS and file.status != 'removed':
            # Truncate large files to avoid token overflow / magic numbers
            patch_content = file.patch
            if patch_content and len(patch_content) > MAX_PATCH_LENGTH:
                patch_content = patch_content[:MAX_PATCH_LENGTH] + '... (truncated)'

            if patch_content:
                code_diffs += f'\n--- FILE: {file.filename} ---\n{patch_content}\n'
                files_analyzed += 1

    if files_analyzed == 0:
        print('✅ No relevant code files found to review.')
        sys.exit(0) # Clean exit (Success)

    # The Polyglot Prompt
    prompt = f'''
    Act as a Senior Polyglot Software Engineer.
    Review the following code changes (git diff) from a Pull Request.

    CONTEXT: The code contains various languages. Identify the language of each file and apply its specific best practices.

    INSTRUCTIONS:
    1. Be concise, constructive, and professional.
    2. Focus on UNIVERSAL software engineering principles:
       - Potential Bugs & Logic Errors.
       - Security Vulnerabilities (Injection, XSS, Exposed Secrets).
       - Performance Issues (N+1 queries, heavy loops, memory leaks).
       - Code Cleanliness (DRY, Naming Conventions, Magic Numbers).
       - Error Handling.
    3. Use 🚨 for critical issues.
    4. Use 💡 for suggestions.
    5. If the code is solid, respond ONLY with: "LGTM (Looks Good To Me) 🚀".

    Format your response in clear Markdown.

    CODE TO REVIEW:
    {code_diffs}
    '''
    try:
        response = model.generate_content(prompt)

        comment_body = f'🤖 **Gemini 2.5 Pro Review** (Analyzed {files_analyzed} files):\n\n{response.text}'
        pr.create_issue_comment(comment_body)
        print('✅ Review posted successfully!')

    except Exception as e:
        print(f'❌ Error generating review: {e}')
        sys.exit(1)

if __name__ == "__main__":
    main()