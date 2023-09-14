import git
import revert-merge

def revert(commit_sha):
    repo = git.Repo(os.getcwd())
    repo.git.revert(commit_sha)

if __name__ == "__main__":
    commit_sha = "${{ github.event.pull_request.merge_commit_sha }}"
    revert(commit_sha)
