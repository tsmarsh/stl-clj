export GIT_COMMITTER_EMAIL=travis@tailoredshapes.com
export GIT_COMMITTER_NAME=Travis CI

echo $floop > key
chmod 600 key
ssh-add key
echo "Checking out master"
git checkout master || exit
echo "merging"
git merge "$TRAVIS_COMMIT" || exit
echo "Success!"
git push
