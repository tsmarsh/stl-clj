export GIT_COMMITTER_EMAIL=travis@tailoredshapes.com
export GIT_COMMITTER_NAME=Travis CI

echo $floop > key
chmod 600 key
ssh-add key
git checkout master || exit
git merge "$TRAVIS_COMMIT" || exit
git push
