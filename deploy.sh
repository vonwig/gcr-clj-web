###########################
# increment version.txt
# package into an OCI image
# deploy to ECR
# update k8s cluster
###########################
BASEDIR=$(dirname "$0")
export DOCKER_CONFIG="$BASEDIR/.docker"

set -e

# increment version
bb -e '(spit "version.txt" (format "v%s" (inc (Integer. (str (second (re-find #"v(\d+)" (str/trim (slurp "version.txt")))))))))'
# build OCI image to docker
clj -Tjib build

# Update K8 cluster
# pushd resources/k8s/deployment
# kustomize edit set image "slimslenderslacks/dockerhub-eks-clj-web:$(cat ../../../version.txt)"
# kustomize build . | kubectl apply -f -
# popd

