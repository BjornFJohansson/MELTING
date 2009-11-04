#!/bin/bash
TMPFOLDER=/tmp/meltingbuild
INNER_ARCHIVE_FOLDER=MELTING5.0.3
ARCHIVE_NAME=MELTING5.0.3
DSTFOLDER=${TMPFOLDER}/${INNER_ARCHIVE_FOLDER}
echo "Preparing temporary folder ${DSTFOLDER}"
mkdir -p ${DSTFOLDER}

echo "Copying sources"
cp -Rf src ${DSTFOLDER}

echo "Copying data"
cp -Rf Data ${DSTFOLDER}

echo "Copying documentations"
cp -Rf doc javadoc ${DSTFOLDER}

echo "Copying binaries"
cp -Rf executable ${DSTFOLDER}

echo "Copying miscellaneous"
cp -Rf testResults ${DSTFOLDER}
cp ChangeLog GeneralPublicLicence Install.unices README build.xml buildProject.sh ${DSTFOLDER}

echo "Adding execution rights"
chmod a+x ${DSTFOLDER}/executable/*
chmod a+x ${DSTFOLDER}/Install.unices
chmod a+x ${DSTFOLDER}/buildProject.sh

cd ${DSTFOLDER}
find -name .svn -prune -exec rm -Rf '{}' \;
cd -

echo "Packing into tgz"
tar -C ${TMPFOLDER} -czf ${ARCHIVE_NAME}.tar.gz ${INNER_ARCHIVE_FOLDER} && echo "\t ${ARCHIVE_NAME}.tar.gz created"
echo "Packing into zip"
cd ${TMPFOLDER}
zip ${ARCHIVE_NAME}.zip ${INNER_ARCHIVE_FOLDER}/ && echo "\t ${ARCHIVE_NAME}.zip done"
cd -

echo "Cleaning"
#rm -Rf ${TMPFOLDER}