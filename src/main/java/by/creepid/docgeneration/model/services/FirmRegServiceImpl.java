package by.creepid.docgeneration.model.services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import by.creepid.docgeneration.dao.ListStreetTypeDAO;
import by.creepid.docgeneration.dao.RegionsDAO;
import by.creepid.docgeneration.model.FirmReg;

public class FirmRegServiceImpl implements ResourceLoaderAware, FirmRegService {

	private ResourceLoader resourceLoader;

	private String choiceTemplate;

	private byte[] choiseImg;

	@Autowired
	private ListStreetTypeDAO listStreetTypeDAO;

	@Autowired
	private RegionsDAO regionsDAO;

	private void initResources() throws IOException {
		InputStream is = resourceLoader.getResource(choiceTemplate)
				.getInputStream();
		choiseImg = IOUtils.toByteArray(is);
	}

	public void fillChoice(FirmReg reg) {
		switch (reg.getType_connection()) {
		case web:
			reg.setWebChoice(choiseImg);
			reg.setOfflineChoice(null);
			break;
		case offline:
			reg.setOfflineChoice(choiseImg);
			reg.setWebChoice(null);
			break;
		default:
			reg.setOfflineChoice(null);
			reg.setWebChoice(null);
			break;
		}

	}

	public void fillRegionStr(FirmReg reg) {
		reg.setRegionStr(regionsDAO.findById(reg.getRegion()).getValue());
	}

	public void fillStreet_typeStr(FirmReg reg) {
		reg.setStreet_typeStr(listStreetTypeDAO.findById(reg.getStreet_type())
				.getValue());
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setChoiceTemplate(String choiceTemplate) {
		this.choiceTemplate = choiceTemplate;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setListStreetTypeDAO(ListStreetTypeDAO listStreetTypeDAO) {
		this.listStreetTypeDAO = listStreetTypeDAO;
	}

	public void setRegionsDAO(RegionsDAO regionsDAO) {
		this.regionsDAO = regionsDAO;
	}
}
