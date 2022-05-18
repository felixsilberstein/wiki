# Either replace the whole .bash_porfile file located at ~/ or take any components
set -o ignoreeof
export HISTCONTROL=ignoreboth:erasedups:ignorespace
export HISTFILESIZE=1000000
export HISTSIZE=1000000

alias ll='ls -alF'
alias gts='git status'
alias gd='git diff --color=auto'
alias ..='cd ..'
alias grep='grep --color'
source ~/.git-prompt.sh
PS1='[\[\033[01;32m\]\t\[\033[00m\]]\[\033[01;34m\]`smart_path`\[\033[00m\]\[\e[0;31m\]`git_info`\[\e[0m\]\$ '

pyclean () {
    find . -type f -name '*.py[co]' -delete -o -type d -name __pycache__ -delete
}

function whereis() {
  BASE_DIR=~/projects
  if [ -n "$2" ]; then
    if [ -d "$BASE_DIR/$2" ];then
      BASE_DIR=$BASE_DIR/$2
    fi
  fi
  if [ -n "$1" ]; then    
    grep -rl --exclude-dir=env --exclude-dir=.git --exclude-dir=packages --exclude-dir=node_modules --exclude-dir=dist --exclude-dir=target "$1" $BASE_DIR
  fi
}
function git_info() {
  G=$(__git_ps1 "(%s)")
  if [ -n "$G" ]; then
    O="$(git remote -v | rev | awk -F/ '{print $2}' | rev| sort -u)"
    echo "[$O:$G]"
  fi
}

function gnew(){
  if [ -n "$1" ]; then          
    B="$1"
    P="$(__git_ps1 %s)"
    git checkout $P && git pull && git checkout -b $B && git push -u origin $B
  fi
}

function gsync() {
  # https://stackoverflow.com/a/2013589
  P=${1:-"develop"}
  B="$(__git_ps1 %s)"
  git checkout $P && git pull && git checkout $B && git merge $P
}

function smart_path() {
  IFS=/
  APP_PATH=`pwd`

  ARR=($APP_PATH)
  TOTAL=${#ARR[@]}
  if [ "$TOTAL" -gt "8" ]; then 
    let "MED = $TOTAL/2"
    echo "${ARR[1]}/../${ARR[$MED]}/../${ARR[$TOTAL-1]}"
  else
    pwd
  fi
}
export PATH="/usr/local/opt/python/libexec/bin:$PATH"

export PATH="/usr/local/sbin:$PATH"
export PATH="/usr/local/opt/mysql@5.7/bin:$PATH"

export PATH=$HOME/tools/bin:$PATH
export PATH=$HOME/tools/nielsensports-util-devtools/bin:$PATH
export DEV_HOME=$HOME
export UMLET_HOME=~/tools/umlet
