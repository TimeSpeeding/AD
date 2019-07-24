using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;
using Group13SSIS.Utility;

namespace Group13SSIS.Controllers
{
    public class HomeController : Controller
    {
        // GET: Register Page
        [HttpGet]
        public ActionResult Register()
        {
            return View();
        }
        // POST: for Admin to add new user
        [HttpPost]
        public ActionResult Register(User user)
        {

            return View();
        }
    }
}